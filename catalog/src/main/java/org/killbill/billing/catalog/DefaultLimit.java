/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.catalog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;

import org.killbill.billing.catalog.api.Limit;
import org.killbill.billing.util.config.catalog.ValidatingConfig;
import org.killbill.billing.util.config.catalog.ValidationError;
import org.killbill.billing.util.config.catalog.ValidationErrors;

@XmlAccessorType(XmlAccessType.NONE)
public class DefaultLimit extends ValidatingConfig<StandaloneCatalog> implements Limit {
    @XmlElement(required = true)
    @XmlIDREF
    private DefaultUnit unit;

    @XmlElement(required = false)
    private Double max;
    
    @XmlElement(required = false)
    private Double min;
    
    
    /* (non-Javadoc)
     * @see org.killbill.billing.catalog.Limit#getUnit()
     */
    @Override
    public DefaultUnit getUnit() {
        return unit;
    }

    /* (non-Javadoc)
     * @see org.killbill.billing.catalog.Limit#getMax()
     */
    @Override
    public Double getMax() {
        return max;
    }

    /* (non-Javadoc)
     * @see org.killbill.billing.catalog.Limit#getMin()
     */
    @Override
    public Double getMin() {
        return min;
    }

    @Override
    public ValidationErrors validate(StandaloneCatalog root, ValidationErrors errors) {
        if(max == null && min == null) {
            errors.add(new ValidationError("max and min cannot both be ommitted",root.getCatalogURI(), Limit.class, ""));
        } else if (max != null && min != null && max.doubleValue() < min.doubleValue()) {
            errors.add(new ValidationError("max must be greater than min",root.getCatalogURI(), Limit.class, ""));
        }

        return errors;
    }

    @Override
    public boolean compliesWith(double value) {
        if (max != null) {
            if (value > max.doubleValue()) {
                return false;
            }
        }
        if (min != null) {
            if (value < min.doubleValue()) {
                return false;
            }
        }
        return true;
    }
}