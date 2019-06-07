package com.regularExpressionChecking;

import static org.junit.jupiter.api.Assertions.*;

class TextConverterTest {

    @org.junit.jupiter.api.Test
    void searchWord() {
        TextConverter textConverter = new TextConverter();
        String text = "Kawa Ława ma 12 linii";
        String phrase = "ka";
        String phraseCaps = "KA";
        int result = textConverter.searchWord(text, phrase, phraseCaps);

        assertEquals(result,1);
    }
}