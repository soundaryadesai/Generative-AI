package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.model.ResumeDetailsDTO;
import org.example.model.ResumeMatchDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResumeJdMatcherService {

    public String getExtractedSkillSet(String temp)
    {

        String skillSetExtractedResume= StringUtils.substringBetween(temp, "Primary skills ", "Operating System");
        return skillSetExtractedResume;
    }

    public List<ResumeMatchDTO> analyzeResume(Set<String> jDKeyWords, List<ResumeDetailsDTO> resumeList)
    {
        List<ResumeMatchDTO> match = new ArrayList<ResumeMatchDTO>();
        int totalKeyWords=jDKeyWords.size();

        for(ResumeDetailsDTO resume : resumeList)
        {
            ResumeMatchDTO resumematch= new ResumeMatchDTO();
            int matchCount=0;
            int percentageMatch=0;
            List<String> matchKeywords= new ArrayList<String>();
            for(String keyWord : jDKeyWords) {
                if(keyWord!=null && keyWord!="" && resume.getResumeDetails().contains(keyWord))
                {
                    matchCount++;
                    matchKeywords.add(keyWord);
                }

            }
            percentageMatch = (matchCount * 100)/totalKeyWords;
            String temp = resume.getResumeName();
            String resumeName = StringUtils.substringBetween(temp, "RESUMES\\", ".pdf");
            resumematch.setResumeName(resumeName);
            resumematch.setPercentageMatch(percentageMatch);
            resumematch.setMatchedKeywords(matchKeywords);
            match.add(resumematch);
        }
        System.out.println("Output at the end of the program " + match);
        return match;
    }

    public Set<String> readJD(String jDPath) throws IOException {
        File file = new File(jDPath);
        PDDocument document = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        String spaceRemovedString=text.replaceAll("\\s+", " ");
        String skillSetExtractedString = getExtractedSkillSet(spaceRemovedString);
        String[] words = skillSetExtractedString.split(",");
        //int count = 0;
        Set<String> keyWords = new HashSet<String>();
        for (String word : words) {
            if (word != null && word!="") {
                String commaRemovedString=word.replace(",", "").trim();
                //word.replace(",", " ");
                keyWords.add(commaRemovedString);
            }
        }
        return keyWords;

    }

    public String readResume (String resumePath) throws IOException
    {
        File file = new File(resumePath);
        PDDocument document = PDDocument.load(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();

        String resumeString = pdfStripper.getText(document);

        document.close();
        String skillSetExtractedResume = getExtractedSkillSet(resumeString);
        return skillSetExtractedResume;

    }
}
