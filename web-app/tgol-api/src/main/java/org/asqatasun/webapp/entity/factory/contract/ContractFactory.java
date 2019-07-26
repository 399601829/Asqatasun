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
package org.asqatasun.webapp.entity.factory.contract;

import java.util.Date;
import java.util.Set;
import org.asqatasun.entity.GenericFactory;
import org.asqatasun.webapp.entity.contract.Contract;
import org.asqatasun.webapp.entity.functionality.Functionality;
import org.asqatasun.webapp.entity.option.OptionElement;
import org.asqatasun.webapp.entity.referential.Referential;
import org.asqatasun.webapp.entity.scenario.Scenario;
import org.asqatasun.webapp.entity.user.User;

/**
 *
 * @author jkowalczyk
 */
public interface ContractFactory extends GenericFactory<Contract> {

    /**
     * 
     * @param label
     * @param beginDate
     * @param endDate
     * @param renewalDate
     * @param price
     * @param functionalitySet
     * @param optionSet
     * @param referentialSet
     * @param scenarioSet
     * @param user
     * @return 
     *      an initialised instance of Contract
     */
    Contract createContract(
            String label,
            Date beginDate,
            Date endDate,
            Date renewalDate,
            Float price,
            Set<Functionality> functionalitySet,
            Set<OptionElement> optionSet,
            Set<Referential> referentialSet,
            Set<Scenario> scenarioSet,
            User user);

}
