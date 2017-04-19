/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.news_app;

/**
 * Created by PriteshJ on 4/16/17.
 * <p>
 * An {@link News} object contains information related to a single news article.
 */
class News {

    /**
     * The name of the section
     */
    private final String mSectionName;

    /**
     * The combined date and time of publication
     */
    private String mWebPublicationDate;

    /**
     * The headline or title of publication
     */
    private final String mWebTitle;

    /**
     * The summary of news article
     */
    private String mTrailText;

    /**
     * The name of writer of news article
     */
    private final String mByLine;


    /**
     * The total number of words in news article
     */
    private final int mWordCount;

    /**
     * A shortened url for the news article
     */
    private final String mShortUrl;

    /**
     * Url of the thumbnail
     */
    private final String mThumbnailUrl;

    /**
     * @param mSectionName        is name of section
     * @param mWebPublicationDate is date of publication of news on web
     * @param mWebTitle           is the headline or title of published news article
     * @param mTrailText          is summary of news article
     * @param mByLine             is name of people who authored the news article
     * @param mWordCount          is number of words in news article
     * @param mShortUrl           is shortened url of news article
     * @param mThumbnailUrl       is url of thumbnail for news article
     */
    public News(String mSectionName, String mWebPublicationDate, String mWebTitle, String mTrailText, String mByLine, int mWordCount, String mShortUrl, String mThumbnailUrl) {
        this.mSectionName = mSectionName;
        this.mWebPublicationDate = mWebPublicationDate;
        this.mWebTitle = mWebTitle;
        this.mTrailText = mTrailText;
        this.mByLine = mByLine;
        this.mWordCount = mWordCount;
        this.mShortUrl = mShortUrl;
        this.mThumbnailUrl = mThumbnailUrl;
    }

    /**
     * @return the name of section
     */
    public String getmSectionName() {
        return mSectionName;
    }

    /**
     * @return the date of publication
     */
    public String getmWebPublicationDate() {
        return mWebPublicationDate;
    }

    /**
     * Updates value of web publication date of news article
     *
     * @param mWebPublicationDate denotes date news article got published
     */
    public void setmWebPublicationDate(String mWebPublicationDate) {
        this.mWebPublicationDate = mWebPublicationDate;
    }

    /**
     * @return the title of news article
     */
    public String getmWebTitle() {
        return mWebTitle;
    }

    /**
     * @return the summary of news article
     */
    public String getmTrailText() {
        return mTrailText;
    }

    /**
     * Updates the value of trail text
     *
     * @param mTrailText denotes summary of news article
     */
    public void setmTrailText(String mTrailText) {
        this.mTrailText = mTrailText;
    }

    /**
     * @return the people who authored news article
     */
    public String getmByLine() {
        return mByLine;
    }

    /**
     * @return total number of words in news article
     */
    public int getmWordCount() {
        return mWordCount;
    }

    /**
     * @return shortened url of news article
     */
    public String getmShortUrl() {
        return mShortUrl;
    }

    /**
     * @return url of thumbnail for news article
     */
    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }
}
