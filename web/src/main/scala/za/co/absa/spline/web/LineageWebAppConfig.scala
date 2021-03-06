/*
 * Copyright 2017 Barclays Africa Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.spline.web

import java.util.Arrays.asList

import za.co.absa.spline.core.storage.DataLineageStorage
import za.co.absa.spline.core.storage.mongo.MongoDataLineageStorage
import org.apache.commons.configuration.{CompositeConfiguration, EnvironmentConfiguration, SystemConfiguration}
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class LineageWebAppConfig {

  import za.co.absa.spline.common.ConfigurationImplicits._

  val confProps = new CompositeConfiguration(asList(
    new SystemConfiguration,
    new EnvironmentConfiguration
  ))

  @Bean def lineageStorage: DataLineageStorage = new MongoDataLineageStorage(
    dbUrl = confProps getRequiredString "spline.mongodb.url",
    dbName = confProps getRequiredString "spline.mongodb.name"
  )
}
