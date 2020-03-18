/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2019  Asqatasun.org
 *
 * This file is part of Asqatasun.
 *
 * Asqatasun is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */
package org.asqatasun.webapp.entity.service.contract;

import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.asqatasun.entity.service.AbstractGenericDataService;
import org.asqatasun.webapp.entity.contract.Contract;
import org.asqatasun.webapp.entity.dao.contract.ContractDAO;
import org.asqatasun.webapp.entity.functionality.Functionality;
import org.asqatasun.webapp.entity.option.OptionElement;
import org.asqatasun.webapp.entity.user.User;

/**
 *
 * @author jkowalczyk
 */
public class ContractDataServiceImpl extends AbstractGenericDataService<Contract, Long>
        implements ContractDataService {

    private static final String URL_OPTION_NAME = "DOMAIN";
    private static final String PRESET_CONTRACT_OPTION_NAME = "PRESET_CONTRACT";
    
    @Override
    public Collection<Contract> getAllContractsByUser(User user) {
        return ((ContractDAO) entityDao).findAllContractsByUser(user);
    }

    @Override 
    public String getUrlFromContractOption(Contract contract) {
        for (OptionElement optionElement : ((ContractDAO) entityDao).read(contract.getId()).getOptionElementSet()) {
            if (StringUtils.equals(URL_OPTION_NAME, optionElement.getOption().getCode())) {
                return optionElement.getValue();
            }
        }
        return "";
    }
    
    @Override
    public String getPresetContractKeyContractOption(Contract contract) {
        for (OptionElement optionElement : 
                ((ContractDAO) entityDao).read(contract.getId()).getOptionElementSet()) {
            if (StringUtils.equals(PRESET_CONTRACT_OPTION_NAME, optionElement.getOption().getCode())) {
                return optionElement.getValue();
            }
        }
        return "";
    }

    @Override
    public boolean doesContractHaveFunctionality(Contract contract, String functionnalityKey) {
        for (Functionality functionality : contract.getFunctionalitySet()) {
            if (StringUtils.equalsIgnoreCase(functionality.getCode(), functionnalityKey)) {
                return true;
            }
        }
        return false;
    }

}
