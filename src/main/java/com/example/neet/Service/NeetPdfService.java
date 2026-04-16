package com.example.neet.Service;

import com.example.neet.Entity.Question;
import com.example.neet.Repo.QuestionRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NeetPdfService {

    @Autowired
    private QuestionRepo repo;

    public void processPdf(MultipartFile file, int year, String subject) {

        try (PDDocument doc = PDDocument.load(file.getInputStream())) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            String[] blocks = text.split("\\n\\s*\\d+\\.\\s");
            int qNo = 1;

            for (String block : blocks) {

                if (!block.contains("(A)") || !block.contains("(B)")) continue;

                String[] lines = block.split("\\n");

                Question q = new Question();
               
                q.setSubject(subject);
                
                q.setQuestion(lines[0]);
                q.setOptionA(lines[1].replace("(A)", ""));
                q.setOptionB(lines[2].replace("(B)", ""));
                q.setOptionC(lines[3].replace("(C)", ""));
                q.setOptionD(lines[4].replace("(D)", ""));
                q.setCorrectAnswer(null);

                repo.save(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
