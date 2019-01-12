package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.util.Objects;

public class UserCount {
    private int number;

    public UserCount(){

    }

    public int getNumber() {
        return number;
    }

    public String getNumbers(){return Integer.toString(number);}

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCount)) return false;
        UserCount userCount = (UserCount) o;
        return number == userCount.number;
    }

    @Override
    public int hashCode() {

        return Objects.hash(number);
    }

    @NonNull
    @Override
    public String toString() {
        return "UserCount{" +
                "number=" + number +
                '}';
    }


}
