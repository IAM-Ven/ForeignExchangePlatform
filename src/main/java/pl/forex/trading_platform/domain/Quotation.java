package pl.forex.trading_platform.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String time;

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;

    @OneToOne(cascade = CascadeType.ALL)
    private BidPriceBucket bidPriceBucket;

    @OneToOne(cascade = CascadeType.ALL)
    private AskPriceBucket askPriceBucket;

    public Quotation(String date, String time, Instrument instrument, BidPriceBucket bidPriceBucket, AskPriceBucket askPriceBucket) {
        this.date = date;
        this.time = time;
        this.instrument = instrument;
        this.bidPriceBucket = bidPriceBucket;
        this.askPriceBucket = askPriceBucket;
    }

    @Override
    public String toString() {
        return "Quotation{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", instrument=" + instrument +
                '}';
    }
}
