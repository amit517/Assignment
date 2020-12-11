package com.team.assignment.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Amit on 11,December,2020
 */
public class ExperienceFilter implements InputFilter {
    private int mIntMin, mIntMax;

    public ExperienceFilter(int minValue, int maxValue) {
        this.mIntMin = minValue;
        this.mIntMax = maxValue;
    }

    public ExperienceFilter(String minValue, String maxValue) {
        this.mIntMin = Integer.parseInt(minValue);
        this.mIntMax = Integer.parseInt(maxValue);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            // Removes string that is to be replaced from destination
            // and adds the new string in.
            String newVal = dest.subSequence(0, dstart)
                    // Note that below "toString()" is the only required:
                    + source.subSequence(start, end).toString()
                    + dest.subSequence(dend, dest.length());
            int input = Integer.parseInt(newVal);
            if (isInRange(mIntMin, mIntMax, input))
                return null;
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}