import csv
import json
import mimetypes
import re
import uuid
from decimal import Decimal
from pathlib import Path
from urllib.error import URLError
from urllib.request import Request, urlopen


REPO_ROOT = Path(__file__).resolve().parents[2]
DEFAULT_CSV_DIR = REPO_ROOT / "csv"
DEFAULT_BACKEND_URL = "http://localhost:8080"


class CBAMDefaultValuesTool:
    """
    Tool for CBAM default values and Java backend calculations.

    Local CSVs are used for:
    - product/CN discovery
    - default value lookup
    - benchmark fallback

    Java backend is used for:
    - default emissions calculation
    - actual emissions calculation when user uploaded CSV
    """

    def __init__(
        self,
        csv_dir: str | Path = DEFAULT_CSV_DIR,
        backend_url: str = DEFAULT_BACKEND_URL,
    ):
        self.csv_dir = Path(csv_dir)
        self.backend_url = backend_url.rstrip("/")

        self.transitional_defaults = self._read_csv(
            "transitional_default_values_2023_2025.csv"
        )
        self.country_defaults = self._read_csv(
            "country_default_values_2026_plus.csv"
        )
        self.benchmarks = self._read_csv("cbam_benchmarks_2026_plus.csv")

    def find_cn_codes_by_description(
        self,
        product_name: str,
        limit: int = 10,
    ) -> list[dict]:
        query = _clean_text(product_name)
        matches: dict[tuple[str, str], dict] = {}

        for row in [*self.country_defaults, *self.transitional_defaults]:
            description = _clean_text(row.get("description", ""))

            if query in description:
                key = (_normalize_cn(row["cn_code"]), row.get("description", ""))

                matches[key] = {
                    "cn_code": row["cn_code"],
                    "description": row.get("description", ""),
                    "sector": row.get("product_group", ""),
                    "source": row.get("source_file", "default_values_csv"),
                }

        for row in self.benchmarks:
            description = _clean_text(row.get("description", ""))

            if query in description:
                key = (_normalize_cn(row["cn_code"]), row.get("description", ""))

                matches.setdefault(
                    key,
                    {
                        "cn_code": row["cn_code"],
                        "description": row.get("description", ""),
                        "sector": row.get("product_group", ""),
                        "source": "cbam_benchmarks_2026_plus.csv",
                    },
                )

        return list(matches.values())[:limit]

    def lookup_by_product_name(
        self,
        product_name: str,
        year: int = 2026,
        country: str = "Turkey",
        limit: int = 10,
    ) -> list[dict]:
        cn_matches = self.find_cn_codes_by_description(
            product_name=product_name,
            limit=limit,
        )

        results = []

        for match in cn_matches:
            values = self.lookup_by_cn_code(
                cn_code=match["cn_code"],
                year=year,
                country=country,
            )
            results.extend(values[:1])

        return results[:limit]

    def lookup_by_cn_code(
        self,
        cn_code: str,
        year: int = 2026,
        country: str = "Turkey",
    ) -> list[dict]:
        normalized_cn = _normalize_cn(cn_code)

        if 2023 <= year <= 2025:
            matches = [
                self._format_transitional(row, year, country)
                for row in self.transitional_defaults
                if _normalize_cn(row["cn_code"]) == normalized_cn
            ]

            if matches:
                return matches

        country_matches = [
            self._format_country_default(row, year)
            for row in self.country_defaults
            if _normalize_cn(row["cn_code"]) == normalized_cn
            and _same_country(row.get("country", ""), country)
            and int(row["year"]) == _effective_year(year)
        ]

        usable_country_matches = [
            row
            for row in country_matches
            if row.get("selected_default_value_tco2e_per_ton")
        ]

        if usable_country_matches:
            return usable_country_matches

        return self._benchmark_fallback(
            normalized_cn=normalized_cn,
            year=year,
            country=country,
        )

    def calculate_default_emissions(
        self,
        cn_code: str,
        export_volume_tons: str | int | float | Decimal,
        year: int = 2026,
        country: str = "Turkey",
    ) -> dict:
        payload = {
            "country": country,
            "cnCode": _normalize_cn(cn_code),
            "year": year,
            "exportVolumeTons": str(export_volume_tons),
        }

        return self._post_json("/api/cbam/default-emissions", payload)

    def calculate_default_emissions_by_product_name(
        self,
        product_name: str,
        export_volume_tons: str | int | float | Decimal,
        year: int = 2026,
        country: str = "Turkey",
    ) -> dict:
        matches = self.lookup_by_product_name(
            product_name=product_name,
            year=year,
            country=country,
            limit=1,
        )

        if not matches:
            raise ValueError(f"No CN code found for product name: {product_name}")

        return self.calculate_default_emissions(
            cn_code=matches[0]["cn_code"],
            export_volume_tons=export_volume_tons,
            year=year,
            country=country,
        )

    def calculate_actual_emissions(
        self,
        cn_code: str,
        export_volume_tons: str | int | float | Decimal,
        year: int = 2026,
        country: str = "Turkey",
        csv_file_path: str | Path | None = None,
    ) -> dict:
        fields = {
            "country": country,
            "cnCode": _normalize_cn(cn_code),
            "year": str(year),
            "exportVolumeTons": str(export_volume_tons),
        }

        if csv_file_path:
            return self._post_multipart(
                path="/api/cbam/actual-emissions",
                fields=fields,
                file_field="file",
                file_path=Path(csv_file_path),
            )

        payload = {
            "country": country,
            "cnCode": _normalize_cn(cn_code),
            "year": year,
            "exportVolumeTons": str(export_volume_tons),
        }

        return self._post_json("/api/cbam/actual-emissions", payload)

    def calculate_actual_emissions_by_product_name(
        self,
        product_name: str,
        export_volume_tons: str | int | float | Decimal,
        year: int = 2026,
        country: str = "Turkey",
        csv_file_path: str | Path | None = None,
    ) -> dict:
        matches = self.lookup_by_product_name(
            product_name=product_name,
            year=year,
            country=country,
            limit=1,
        )

        if not matches:
            raise ValueError(f"No CN code found for product name: {product_name}")

        return self.calculate_actual_emissions(
            cn_code=matches[0]["cn_code"],
            export_volume_tons=export_volume_tons,
            year=year,
            country=country,
            csv_file_path=csv_file_path,
        )

    def search_by_description(self, query: str, limit: int = 10) -> list[dict]:
        return self.find_cn_codes_by_description(query, limit=limit)

    def _read_csv(self, filename: str) -> list[dict]:
        path = self.csv_dir / filename

        if not path.exists():
            raise FileNotFoundError(f"Missing CBAM data file: {path}")

        with path.open(newline="", encoding="utf-8") as handle:
            rows = list(csv.DictReader(handle))

        for row in rows:
            row["source_file"] = filename

        return rows

    def _format_transitional(self, row: dict, year: int, country: str) -> dict:
        selected = _decimal_or_none(row.get("default_total_tco2e_per_ton"))

        return {
            "country": country,
            "cn_code": _normalize_cn(row["cn_code"]),
            "description": row.get("description", ""),
            "sector": row.get("product_group", ""),
            "year": year,
            "direct_default_tco2e_per_ton": _decimal_or_none(
                row.get("default_direct_tco2e_per_ton")
            ),
            "indirect_default_tco2e_per_ton": _decimal_or_none(
                row.get("default_indirect_tco2e_per_ton")
            ),
            "total_default_tco2e_per_ton": selected,
            "selected_default_value_tco2e_per_ton": selected,
            "source": row["source_file"],
        }

    def _format_country_default(self, row: dict, year: int) -> dict:
        selected = _decimal_or_none(row.get("adopted_default_total_tco2e_per_ton"))

        return {
            "country": row.get("country", ""),
            "cn_code": _normalize_cn(row["cn_code"]),
            "description": row.get("description", ""),
            "sector": row.get("product_group", ""),
            "year": year,
            "direct_default_tco2e_per_ton": _decimal_or_none(
                row.get("default_direct_tco2e_per_ton")
            ),
            "indirect_default_tco2e_per_ton": _decimal_or_none(
                row.get("default_indirect_tco2e_per_ton")
            ),
            "total_default_tco2e_per_ton": _decimal_or_none(
                row.get("default_total_tco2e_per_ton")
            ),
            "selected_default_value_tco2e_per_ton": selected,
            "underlying_cbam_benchmark_route": row.get(
                "underlying_cbam_benchmark_route"
            )
            or None,
            "source": row["source_file"],
        }

    def _benchmark_fallback(
        self,
        normalized_cn: str,
        year: int,
        country: str,
    ) -> list[dict]:
        matches = [
            row
            for row in self.benchmarks
            if _normalize_cn(row["cn_code"]) == normalized_cn
            and _decimal_or_none(row.get("bm_tco2e_per_ton")) is not None
        ]

        matches.sort(
            key=lambda row: Decimal(_decimal_or_none(row["bm_tco2e_per_ton"]) or "0"),
            reverse=True,
        )

        return [
            {
                "country": country,
                "cn_code": _normalize_cn(row["cn_code"]),
                "description": row.get("description", ""),
                "sector": row.get("product_group", ""),
                "year": year,
                "direct_default_tco2e_per_ton": None,
                "indirect_default_tco2e_per_ton": None,
                "total_default_tco2e_per_ton": _decimal_or_none(
                    row["bm_tco2e_per_ton"]
                ),
                "selected_default_value_tco2e_per_ton": _decimal_or_none(
                    row["bm_tco2e_per_ton"]
                ),
                "benchmark_column": row.get("benchmark_column", ""),
                "production_route": row.get("production_route") or None,
                "source": row["source_file"],
            }
            for row in matches
        ]

    def _post_json(self, path: str, payload: dict) -> dict:
        body = json.dumps(payload).encode("utf-8")

        request = Request(
            f"{self.backend_url}{path}",
            data=body,
            headers={"Content-Type": "application/json"},
            method="POST",
        )

        try:
            with urlopen(request, timeout=20) as response:
                return json.loads(response.read().decode("utf-8"))
        except URLError as exc:
            raise ConnectionError(
                f"Could not reach Java backend at {self.backend_url}. "
                "Start the backend first, then retry the calculation."
            ) from exc

    def _post_multipart(
        self,
        path: str,
        fields: dict[str, str],
        file_field: str,
        file_path: Path,
    ) -> dict:
        if not file_path.exists():
            raise FileNotFoundError(f"CSV file not found: {file_path}")

        boundary = f"----CBAMBoundary{uuid.uuid4().hex}"
        body_parts: list[bytes] = []

        for name, value in fields.items():
            body_parts.append(f"--{boundary}\r\n".encode())
            body_parts.append(
                f'Content-Disposition: form-data; name="{name}"\r\n\r\n'.encode()
            )
            body_parts.append(str(value).encode())
            body_parts.append(b"\r\n")

        mime_type = mimetypes.guess_type(str(file_path))[0] or "text/csv"

        body_parts.append(f"--{boundary}\r\n".encode())
        body_parts.append(
            (
                f'Content-Disposition: form-data; name="{file_field}"; '
                f'filename="{file_path.name}"\r\n'
            ).encode()
        )
        body_parts.append(f"Content-Type: {mime_type}\r\n\r\n".encode())
        body_parts.append(file_path.read_bytes())
        body_parts.append(b"\r\n")
        body_parts.append(f"--{boundary}--\r\n".encode())

        body = b"".join(body_parts)

        request = Request(
            f"{self.backend_url}{path}",
            data=body,
            headers={
                "Content-Type": f"multipart/form-data; boundary={boundary}",
                "Content-Length": str(len(body)),
            },
            method="POST",
        )

        try:
            with urlopen(request, timeout=60) as response:
                return json.loads(response.read().decode("utf-8"))
        except URLError as exc:
            raise ConnectionError(
                f"Could not reach Java backend at {self.backend_url}. "
                "Start the backend first, then retry the calculation."
            ) from exc


def _normalize_cn(value: str) -> str:
    return re.sub(r"\s+", "", str(value or "")).strip()


def _clean_text(value: str) -> str:
    return re.sub(r"\s+", " ", str(value or "")).casefold().strip()


def _same_country(left: str, right: str) -> bool:
    return _normalize_country(left) == _normalize_country(right)


def _normalize_country(value: str) -> str:
    text = str(value or "").casefold().replace("ü", "u")
    return "turkiye" if text in {"turkey", "turkiye"} else text


def _effective_year(year: int) -> int:
    if year <= 2026:
        return 2026

    if year == 2027:
        return 2027

    return 2028


def _decimal_or_none(value: str | None) -> str | None:
    text = str(value or "").strip().replace(",", ".")

    if text in {"", "-", "–", "N/A", "n/a", "see below"}:
        return None

    return str(Decimal(text))