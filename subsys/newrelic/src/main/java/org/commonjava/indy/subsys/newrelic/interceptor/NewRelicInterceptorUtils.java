/**
 * Copyright (C) 2011-2020 Red Hat, Inc. (https://github.com/Commonjava/indy)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.indy.subsys.newrelic.interceptor;

import org.commonjava.indy.measure.annotation.MetricWrapperNamed;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.Supplier;

public class NewRelicInterceptorUtils
{

    public static String getMetricNameFromParam( InvocationContext context )
    {
        String name = null;

        Method method = context.getMethod();
        Parameter[] parameters = method.getParameters();
        for ( int i=0; i<parameters.length; i++)
        {
            Parameter param = parameters[i];
            MetricWrapperNamed annotation = param.getAnnotation( MetricWrapperNamed.class );
            if ( annotation != null )
            {
                Object pv = context.getParameters()[i];
                if ( pv instanceof Supplier )
                {
                    name = String.valueOf( ( (Supplier) pv ).get() );
                }
                else
                {
                    name = String.valueOf( pv );
                }

                break;
            }
        }

        return name;
    }

}
