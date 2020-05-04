package org.izce.recipe.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UnitOfMeasureCommand {
    private Long id;
    private String uom;
    
    public UnitOfMeasureCommand(String idText) {
    	this.id = Long.parseLong(idText);
    }
}