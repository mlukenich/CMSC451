"""Generate a single-file Project 2 markdown report with SVG graphs embedded inline.

Usage:
    python3 Project2/embed_svgs_in_report.py
"""

from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
PROJECT2 = ROOT / "Project2"
SOURCE = PROJECT2 / "Project2_Consolidated_Report.md"
OUTPUT = PROJECT2 / "Project2_Consolidated_Report_Embedded.md"

IMAGE_RE = re.compile(r"!\[(?P<alt>[^\]]*)\]\((?P<path>[^)]+)\)")


def embed_svg_images(markdown_text: str) -> str:
    def _replace(match: re.Match[str]) -> str:
        alt = match.group("alt").strip()
        rel_path = match.group("path").strip()
        svg_path = (PROJECT2 / rel_path).resolve()

        if svg_path.suffix.lower() != ".svg" or not svg_path.exists():
            return match.group(0)

        svg = svg_path.read_text(encoding="utf-8").strip()
        caption = alt or svg_path.name

        return (
            "\n"
            f"<figure>\n{svg}\n"
            f"<figcaption><em>{caption}</em></figcaption>\n"
            "</figure>\n"
        )

    return IMAGE_RE.sub(_replace, markdown_text)


def main() -> None:
    source_text = SOURCE.read_text(encoding="utf-8")
    embedded = embed_svg_images(source_text)

    preface = (
        "# Project 2 Consolidated Report (Embedded SVG Edition)\n\n"
        "This file is auto-generated from `Project2_Consolidated_Report.md` and has all SVG"
        " graph images embedded inline for single-file submission.\n\n"
        "---\n\n"
    )

    OUTPUT.write_text(preface + embedded, encoding="utf-8")
    print(f"Wrote {OUTPUT}")


if __name__ == "__main__":
    main()
