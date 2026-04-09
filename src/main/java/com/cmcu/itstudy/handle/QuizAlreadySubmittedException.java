package com.cmcu.itstudy.handle;

public class QuizAlreadySubmittedException extends RuntimeException {

    public QuizAlreadySubmittedException(String message) {
        super(message);
    }
}
