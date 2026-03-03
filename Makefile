.PHONY: report analyze embed clean-report

report: analyze embed

analyze:
	python3 Project2/analyze_results.py

embed:
	python3 Project2/embed_svgs_in_report.py

clean-report:
	rm -f Project2/Project2_Consolidated_Report_Embedded.md
