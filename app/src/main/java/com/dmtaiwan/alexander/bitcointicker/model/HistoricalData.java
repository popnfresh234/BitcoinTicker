package com.dmtaiwan.alexander.bitcointicker.model;

/**
 * Created by Alexander on 8/31/2017.
 */

public class HistoricalData {
    private String FirstValueInArray;
    private Data[] Data;
    private String TimeFrom;
    private String Type;
    private String Response;
    private ConversionType ConversionType;
    private String TimeTo;
    private String Aggregated;

    public String getFirstValueInArray() {
        return FirstValueInArray;
    }

    public void setFirstValueInArray(String FirstValueInArray) {
        this.FirstValueInArray = FirstValueInArray;
    }

    public Data[] getData() {
        return Data;
    }

    public void setData(Data[] Data) {
        this.Data = Data;
    }

    public String getTimeFrom() {
        return TimeFrom;
    }

    public void setTimeFrom(String TimeFrom) {
        this.TimeFrom = TimeFrom;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ConversionType getConversionType() {
        return ConversionType;
    }

    public void setConversionType(ConversionType ConversionType) {
        this.ConversionType = ConversionType;
    }

    public String getTimeTo() {
        return TimeTo;
    }

    public void setTimeTo(String TimeTo) {
        this.TimeTo = TimeTo;
    }

    public String getAggregated() {
        return Aggregated;
    }

    public void setAggregated(String Aggregated) {
        this.Aggregated = Aggregated;
    }

    public class ConversionType {
        private String conversionSymbol;
        private String type;

        public String getConversionSymbol() {
            return conversionSymbol;
        }

        public void setConversionSymbol(String conversionSymbol) {
            this.conversionSymbol = conversionSymbol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


    public class Data {
        private String open;
        private String time;
        private String volumeto;
        private String volumefrom;
        private String high;
        private String low;
        private String close;

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getVolumeto() {
            return volumeto;
        }

        public void setVolumeto(String volumeto) {
            this.volumeto = volumeto;
        }

        public String getVolumefrom() {
            return volumefrom;
        }

        public void setVolumefrom(String volumefrom) {
            this.volumefrom = volumefrom;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }
    }
}