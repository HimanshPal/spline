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

package za.co.absa.spline.web.rest.controller

import java.util.UUID

import za.co.absa.spline.core.storage.DataLineageStorage
import za.co.absa.spline.web.salat.JSONSalatContext._
import za.co.absa.spline.web.salat.StringJSONConverters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, ResponseBody}

@Controller
class LineageController @Autowired()
(
  val storage: DataLineageStorage
) {

  import StringJSONConverters._

  @RequestMapping(path = Array("/lineage/descriptors"), method = Array(GET))
  @ResponseBody
  def lineageDescriptors: String = storage.list().toSeq.toJsonArray

  @RequestMapping(path = Array("/lineage/{id}"), method = Array(GET))
  @ResponseBody
  def lineage(@PathVariable("id") id: UUID): String = (storage load id).get.toJson
}
