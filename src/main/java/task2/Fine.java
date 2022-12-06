package task2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Fine implements Comparable {

    private String type;

    private BigDecimal amount;

    public Fine(String type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    public Fine() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int compareTo(Object o) {
        return this.amount.compareTo(((Fine) o).amount);
    }
}
