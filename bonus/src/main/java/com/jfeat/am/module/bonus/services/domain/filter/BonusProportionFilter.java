package com.jfeat.am.module.bonus.services.domain.filter;

import com.jfeat.am.module.bonus.services.gen.persistence.model.BonusProportion;
import com.jfeat.crud.plus.CRUDFilter;


public class BonusProportionFilter
        extends Object
        implements CRUDFilter<BonusProportion> {
    private String[] ignoreFields = new String[0];
    private String[] updateIgnoreFields = new String[0];


    public void filter(BonusProportion entity, boolean insertOrUpdate) {
        if (insertOrUpdate) ;
    }


    public String[] ignore(boolean retrieveOrUpdate) {

        if (retrieveOrUpdate) {

            return this.ignoreFields;
        }


        return this.updateIgnoreFields;
    }
}

