package com.aacreations.countryandcapitals;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class CountryCapital implements Parcelable {

    private static final String TAG = "CountryCapital";

    private int testId = 0;
    private String countryName;
    private String capitalName;
    private String continent;
    private String colour;
    private boolean correct = false;
    private String answerSent = "";
    private String flagId;

    private int questionsCorrect = 0;
    private int questionsWrong = 0;
    private int totalQuestions = 0;
    private String percentage = "0%";
    private long date = 0;
    private String time = "00:00:00";
    private String timeTaken = "00:00";
    private String passFail = "Fail";

    public CountryCapital(String countryName, String capitalName, String continent, String colour, String flagID) {
        this.countryName = countryName;
        this.capitalName = capitalName;
        this.continent = continent;
        this.colour = colour;
        this.flagId = flagID;
    }

    public CountryCapital(int testId, int questionsCorrect, int questionsWrong, int totalQuestions, @NonNull String percentage, long date, String time, String timeTaken) {
        this.testId = testId;
        this.questionsCorrect = questionsCorrect;
        this.questionsWrong = questionsWrong;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.date = date;
        this.time = time;
        this.timeTaken = timeTaken;

        int percentageInNumber = Integer.parseInt(percentage.substring(0, percentage.length() - 3));
        passFail = percentageInNumber >= 70 ? "Pass" : "Fail";
    }

    public CountryCapital(int testId, String countryName, String userAnswer, String correctAnswer, int questionsCorrect, int questionsWrong, int totalQuestions, @NonNull String percentage, long date, String time, String timeTaken) {
        this.testId = testId;
        this.countryName = countryName;
        answerSent = userAnswer;
        this.capitalName = correctAnswer;
        this.questionsCorrect = questionsCorrect;
        this.questionsWrong = questionsWrong;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.date = date;
        this.time = time;
        this.timeTaken = timeTaken;

        int percentageInNumber = Integer.parseInt(percentage.substring(0, percentage.length() - 3));
        passFail = percentageInNumber >= 70 ? "Pass" : "Fail";
    }

    protected CountryCapital(@NonNull Parcel in) {
        testId = in.readInt();
        countryName = in.readString();
        capitalName = in.readString();
        continent = in.readString();
        colour = in.readString();
        correct = in.readByte() != 0;
        answerSent = in.readString();
        flagId = in.readString();
        questionsCorrect = in.readInt();
        questionsWrong = in.readInt();
        totalQuestions = in.readInt();
        percentage = in.readString();
        date = in.readLong();
        time = in.readString();
        timeTaken = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(testId);
        dest.writeString(countryName);
        dest.writeString(capitalName);
        dest.writeString(continent);
        dest.writeString(colour);
        dest.writeByte((byte) (correct ? 1 : 0));
        dest.writeString(answerSent);
        dest.writeString(flagId);
        dest.writeInt(questionsCorrect);
        dest.writeInt(questionsWrong);
        dest.writeInt(totalQuestions);
        dest.writeString(percentage);
        dest.writeLong(date);
        dest.writeString(time);
        dest.writeString(timeTaken);
        dest.writeString(passFail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CountryCapital> CREATOR = new Creator<CountryCapital>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public CountryCapital createFromParcel(Parcel in) {
            return new CountryCapital(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public CountryCapital[] newArray(int size) {
            return new CountryCapital[size];
        }
    };

    public String ttoString() {
        return "CountryCapital{" +
                "testId=" + testId +
                ", countryName='" + countryName + '\'' +
                ", capitalName='" + capitalName + '\'' +
                ", continent='" + continent + '\'' +
                ", colour='" + colour + '\'' +
                ", correct=" + correct +
                ", answerSent='" + answerSent + '\'' +
                ", flagId='" + flagId + '\'' +
                ", questionsCorrect=" + questionsCorrect +
                ", questionsWrong=" + questionsWrong +
                ", totalQuestions=" + totalQuestions +
                ", percentage='" + percentage + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                ", passFail='" + passFail + '\'' +
                '}';
    }

    // ==========================================================================================
    // GETTERS
    // ==========================================================================================

    public int getTestId() {
        return testId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCapitalName() {
        return capitalName;
    }

    public String getContinent() {
        return continent;
    }

    public String getColour() {
        return colour;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getAnswerSent() {
        return answerSent;
    }

    public String getFlagId() {
        return flagId;
    }

    public int getQuestionsCorrect() {
        return questionsCorrect;
    }

    public int getQuestionsWrong() {
        return questionsWrong;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public String getPercentage() {
        return percentage;
    }

    public long getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public String getPassFail() {
        return passFail;
    }

    // ==========================================================================================
    // SETTERS
    // ==========================================================================================


    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setAnswerSent(String answerSent) {
        this.answerSent = answerSent;
    }

    public void setQuestionsCorrect(int questionsCorrect) {
        this.questionsCorrect = questionsCorrect;
    }

    public void setQuestionsWrong(int questionsWrong) {
        this.questionsWrong = questionsWrong;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setPassFail(String passFail) {
        this.passFail = passFail;
    }
}
