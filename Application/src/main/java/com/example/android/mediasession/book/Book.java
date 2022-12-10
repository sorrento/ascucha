package com.example.android.mediasession.book;

import com.example.android.mediasession.BookSumCallback;
import com.example.android.mediasession.Preferences;
import com.example.android.mediasession.ReaderEvents;
import com.parse.FindCallback;
import com.parse.ParseException;
import android.util.Log;


public class Book {
    private BookSummary bookSummary = null;

    private ChapterService chapterService;

    /**
     * @param bookId
     * @param chapterId    si se pone -1 se le asignará un capitulo random
     * @param localStorage
     */
    public Book(final int bookId, final int chapterId, final boolean localStorage,
                final Preferences myPrefs, final ReaderEvents re) {

        BookSumCallback cbSummary = new BookSumCallback() {
            @Override
            public void onReceived(BookSummary bookSum) {
                bookSummary = bookSum;

                chapterService = new ChapterService(bookId, chapterId, bookSummary.getNChapters(),
                        localStorage, myPrefs, new CSEvents() {
                    @Override
                    public void serviceReady() {
                        re.bookReady();
                    }

                    @Override
                    public void bookEnded() {
                        re.bookEnded();
                    }

                    @Override
                    public void error(String text, Exception e) {
                        re.error(text, e);
                    }

                });

            }

            @Override
            public void onError(String text, ParseException e) {
                Log.d("MH", text );
                Log.d("MH", e.getMessage() );
            }
        };

        ParseHelper.getBookSummary(bookId, localStorage, cbSummary);
    }

    public Chapter getCurrentChapter() {
        return chapterService.giveMeSame();
    }

    public String getBookName() {
        return bookSummary.getTitle();
    }


    //OLD
    public String getBookNameFake() {
        return bookSummary.fakeTitle();
    }

    public String getBookAuthor() {
        return bookSummary.getAuthor();
    }

    public String getBookAuthorFake() {
        return bookSummary.getFakeAuthor();
    }

    public int getCurrentChapterId() {
        return chapterService.getCurrentChapterId();
    }

    public void setCurrentChapterId(int i) {
        chapterService.forceChapterId(i);
    }

    public BookSummary getBookSummary() {
        return bookSummary;
    }

    public String getLanguage() {
        return bookSummary.getLanguage();
    }

    public Chapter getNextChapter() {
        return chapterService.giveMeNext();
    }

    public int getBookId() {
        //return chapterService.getCurrentChapterId();
        return bookSummary.getId();
    }

    public void getChapters(int chapterIdIni, int chapterIdFin, FindCallback<Chapter> cb) {
        chapterService.getChapters(chapterIdIni, chapterIdFin, cb);
    }
}