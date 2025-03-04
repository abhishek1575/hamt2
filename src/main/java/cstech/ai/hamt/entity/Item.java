package cstech.ai.hamt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="item" , uniqueConstraints=@UniqueConstraint(columnNames="sap_no"))//
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String category;
    @NotNull
    private String subCategory;
    @NotNull
    private String value;

    private String description;

    private String manufacturer;

    private String package_box;

    private String location;

    private String mpn;

    private Long sap_no;
    @NotNull
    @PositiveOrZero
    private Long stock;

    private boolean isDeleted = false;

    @Column(name = "is_returnable")
    @JsonProperty("isReturnable")
    private boolean isReturnable;

    public boolean isReturnable() {
        return isReturnable;
    }

    public void setReturnable(boolean isReturnable) {
        this.isReturnable = isReturnable;
    }

}
