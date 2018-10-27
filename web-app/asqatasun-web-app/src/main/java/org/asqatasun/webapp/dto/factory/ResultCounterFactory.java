/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2015  Asqatasun.org
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
package org.asqatasun.webapp.dto.factory;

import org.asqatasun.webapp.dto.ResultCounterImpl;
import org.asqatasun.webapp.dto.data.ResultCounter;
import org.springframework.stereotype.Component;

/**
 * 
 * @author jkowalczyk
 */
@Component("resultCounterFactory")
public final class ResultCounterFactory {

    /**
     * The holder that handles the unique instance of ResultCounterFactory
     */
    private static class ResultCounterFactoryHolder {
        private static final ResultCounterFactory INSTANCE = new ResultCounterFactory();
    }

    /**
     * Private constructor
     */
    protected ResultCounterFactory() {}

    /**
     * Singleton pattern based on the "Initialization-on-demand
     * holder idiom". See @http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom
     * @return the unique instance of ResultCounterFactory
     */
    public static ResultCounterFactory getInstance() {
        return ResultCounterFactoryHolder.INSTANCE;
    }

    public ResultCounter getResultCounter(){
        return new ResultCounterImpl();
    }

}