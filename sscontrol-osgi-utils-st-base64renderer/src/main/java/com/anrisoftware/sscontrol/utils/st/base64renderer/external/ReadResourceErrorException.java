package com.anrisoftware.sscontrol.utils.st.base64renderer.external;

/*-
 * #%L
 * sscontrol-osgi - utils-st-base64renderer
 * %%
 * Copyright (C) 2016 - 2018 Advanced Natural Research Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.IOException;

import com.anrisoftware.sscontrol.types.app.external.AppException;

/**
 *
 * @author Erwin Müller, erwin.mueller@deventm.de
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ReadResourceErrorException extends AppException {

    public ReadResourceErrorException(IOException cause, Object resource,
            String formatString) {
        super("Error read resource", cause);
        addContextValue("resource", resource.toString());
        addContextValue("format", formatString);
    }

}
