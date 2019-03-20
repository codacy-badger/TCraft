package com.dkaishu.tcraftlib.location;

import android.location.Criteria;

/**
 * 定位属性
 * Created by dks on 2018/10/31.
 */

public class LocationUpdateProperty {
    public final static long  MIN_TIME_MILLISECOND_DEF = 1000;//
    public final static float MIN_DISTANCE_METER_DEF   = 10;

    private long  mMinTime     = MIN_TIME_MILLISECOND_DEF;
    private float mMinDistance = MIN_DISTANCE_METER_DEF;
    private Criteria criteria = new Criteria();

    public long getmMinTime() {
        return mMinTime;
    }

    public float getmMinDistance() {
        return mMinDistance;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    private LocationUpdateProperty(Builder builder) {
        mMinTime = builder.mMinTime;
        mMinDistance = builder.mMinDistance;
        criteria = builder.criteria;
    }

    public static final class Builder {
        private long     mMinTime;
        private float    mMinDistance;
        private Criteria criteria;

        public Builder() {
        }

        public Builder mMinTime(long val) {
            mMinTime = val;
            return this;
        }

        public Builder mMinDistance(float val) {
            mMinDistance = val;
            return this;
        }

        public Builder criteria(Criteria val) {
            criteria = val;
            return this;
        }

        public LocationUpdateProperty build() {
            return new LocationUpdateProperty(this);
        }
    }
}
