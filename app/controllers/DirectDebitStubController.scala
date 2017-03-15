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
import play.api.libs.json._
import play.api.mvc._
import uk.gov.hmrc.ssttp.desstub.models.{DDIPPRequest, DDIRequest, DirectDebitInstruction, KnownFact, PaymentPlan}

class DirectDebitStubController @Inject()() extends ResponseHandling {

  implicit val knownFactReads = Json.reads[KnownFact]
  implicit val ddInstructionReads = Json.reads[DirectDebitInstruction]

  implicit val paymentPlanReads = Json.reads[PaymentPlan]

  implicit val ddiRequestReads = Json.reads[DDIRequest]
  implicit val ddiPPRequestReads = Json.reads[DDIPPRequest]

  def generateDDI(credentialId: String) = Action { implicit request =>

    def sendDDI(dDIRequest: DDIRequest): Result = {
      if (dDIRequest.isValid) {
        credentialId match {
          case "cred-id-543212300016" | "543212300016" => Ok(JsObject {
            Seq(
              "processingDate" -> JsString("2001-12-17T09:30:47Z"),
              "directDebitInstruction" -> JsArray(Nil)
            )
          })
          case _ => Ok(Json.parse(getClass.getResourceAsStream("/DDI.json")))
        }
      } else {
        yourSubmissionContainsErrors
      }
    }

    if (request.headers.get("AUTHORIZATION").isEmpty)
      Unauthorized("No authorization header present")
    else
      credentialId.toLowerCase match {
        case credId if !(credId.length >= 1 && credId.length <= 25) => yourSubmissionContainsErrors
        case "0000000000" => NotFound(stdBody("BP not found", "002"))
        case _ =>
          request.body.asJson match {
            case Some(json) => json.validate[DDIRequest] match {
              case s: JsSuccess[DDIRequest] => sendDDI(s.get)
              case JsError(e) =>
                Logger.warn(
                  {
                    "JSON Validation Error " :: e.toList.map { x => x._1 + ": " + x._2 }
                  }.mkString("\n   ")
                )
                yourSubmissionContainsErrors
            }
            case _ => yourSubmissionContainsErrors
          }
      }
  }

  def generateDDIPP(credentialId: String) = Action { implicit request =>

    def sendDDIPP(ddiPPRequest: DDIPPRequest): Result = {
      if (ddiPPRequest.isValid) Created(Json.parse(getClass.getResourceAsStream("/DDIPP.json"))) else yourSubmissionContainsErrors
    }

    if (request.headers.get("AUTHORIZATION").isEmpty)
      Unauthorized("No authorization header present")
    else
      credentialId.toLowerCase match {
        case credId if !(credId.length >= 1 && credId.length <= 25) => yourSubmissionContainsErrors
        case _ =>
          request.body.asJson match {
            case Some(json) => json.validate[DDIPPRequest] match {
              case s: JsSuccess[DDIPPRequest] => sendDDIPP(s.get)
              case JsError(e) =>
                Logger.warn(
                  {
                    "JSON Validation Error " :: e.toList.map { x => x._1 + ": " + x._2 }
                  }.mkString("\n   ")
                )
                invalidJson
            }
            case _ => yourSubmissionContainsErrors
          }
      }
  }

}
