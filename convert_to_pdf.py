from playwright.sync_api import sync_playwright

html_path = r"C:\Users\mluke\Downloads\CMSC451_Homework4_Solutions.html"
pdf_path = r"C:\Users\mluke\Downloads\CMSC451_Homework4_Solutions.pdf"

with sync_playwright() as p:
    browser = p.chromium.launch()
    page = browser.new_page()
    page.goto(f"file:///{html_path}")
    page.pdf(path=pdf_path, format="Letter", margin={"top": "0.5in", "bottom": "0.5in", "left": "0.5in", "right": "0.5in"})
    browser.close()

print(f"PDF saved to: {pdf_path}")
