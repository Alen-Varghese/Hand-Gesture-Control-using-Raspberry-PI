import PyPDF2
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from docx import Document

# Function to extract text from a PDF file
def extract_text_from_pdf(pdf_file):
    text = ""
    with open(pdf_file, 'rb') as file:
        pdf_reader = PyPDF2.PdfFileReader(file)
        for page_num in range(pdf_reader.numPages):
            page = pdf_reader.getPage(page_num)
            text += page.extractText()
    return text

# Function to calculate similarity between two text documents
def calculate_similarity(text1, text2):
    # Tokenize the text and remove stopwords
    stop_words = set(stopwords.words("english"))
    words1 = [word.lower() for word in word_tokenize(text1) if word.isalpha() and word.lower() not in stop_words]
    words2 = [word.lower() for word in word_tokenize(text2) if word.isalpha() and word.lower() not in stop_words]

    # Calculate Jaccard similarity
    common_words = len(set(words1).intersection(words2))
    total_words = len(set(words1).union(words2))
    similarity = common_words / total_words if total_words > 0 else 0

    return similarity

def create_report(text1, text2, similarity):
    doc = Document()
    doc.add_heading('Handwriting Plagiarism Detection Report', 0)

    doc.add_heading('PDF 1', level=1)
    doc.add_paragraph(text1)

    doc.add_heading('PDF 2', level=1)
    doc.add_paragraph(text2)

    doc.add_heading('Plagiarism Ratio', level=1)
    doc.add_paragraph(f'The plagiarism ratio between the two PDFs is {similarity *100:.2f}%')

    doc.save('plagiarism_report.docx')

# Main function
def main():
    # User input for two PDF files
    pdf_file1 = input("Enter the first PDF file name: ")
    pdf_file2 = input("Enter the second PDF file name: ")

    # Extract text from PDF files
    text1 = extract_text_from_pdf(pdf_file1)
    text2 = extract_text_from_pdf(pdf_file2)

    # Calculate similarity
    similarity = calculate_similarity(text1, text2)

    create_report(text1,text2,similarity)
    print('Plagiarism report has been generated as "plagiarism_report.docx"')

if __name__ == "__main__":
    main()
