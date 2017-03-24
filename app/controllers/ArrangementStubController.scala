/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.ssttp.desstub.controllers

import javax.inject.Inject

import play.api.Logger
import play.api.mvc._
import uk.gov.hmrc.ssttp.desstub.models._

class ArrangementStubController @Inject()() extends ResponseHandling {

  /**
    * Represents the ttp arrangement DES endpoint which is called by the Time To Pay Arrangement service
    * Carries out a number of validation checks on the data provided and returns any errors if any or
    * Returns an accepted response if successful
    */
  def submitArrangement(utr: String) = Action { implicit request =>

    import play.api.libs.json._
    implicit val ddReads = Json.reads[DebitDetails]
    implicit val ttpArrReads = Json.reads[TTPArrangement]
    implicit val lacReads = Json.reads[LetterAndControl]
    implicit val arrangementReads = Json.reads[Arrangement]

    def sendArrangement(arrangement: Arrangement): Result = {
      utr match {
        case PreprogrammedResult(r) => r
        case _ if !arrangement.isValid => invalidJson
        case _ => Accepted("")
      }
    }

    request.headers.get("Environment") match {
      case None => Unauthorized("No environment header present")
      case Some(_) => request.headers.get(AUTHORIZATION) match {
        case None => Unauthorized("No authorization header present")
        case Some(_) => request.body.asJson match {
          case Some(json) => json.validate[Arrangement] match {
            case s: JsSuccess[Arrangement] => sendArrangement(s.get)
            case JsError(e) => {
              Logger.warn(
                {"JSON Validation Error " :: e.toList.map{x => x._1 + ": " + x._2}}.mkString("\n   "))
              invalidJson
            }
          }
          case None => yourSubmissionContainsErrors
        }
      }
    }
  }
}
