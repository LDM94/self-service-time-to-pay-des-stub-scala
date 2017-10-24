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

import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers.AUTHORIZATION
import uk.gov.hmrc.ssttp.desstub.models.{Arrangement, DDIPPRequest, DDIRequest, DebitDetails, DirectDebitInstruction, KnownFact, LetterAndControl, PaymentPlan, TTPArrangement}

package object testData {

  implicit val knownFactReads = Json.reads[KnownFact]
  implicit val ddInstructionReads = Json.reads[DirectDebitInstruction]

  implicit val paymentPlanReads = Json.reads[PaymentPlan]

  implicit val ddiRequestReads = Json.reads[DDIRequest]
  implicit val ddiPPRequestReads = Json.reads[DDIPPRequest]

  implicit val ddReads = Json.reads[DebitDetails]
  implicit val ttpArrReads = Json.reads[TTPArrangement]
  implicit val lacReads = Json.reads[LetterAndControl]
  implicit val arrangementReads = Json.reads[Arrangement]

  def loadFile(file: String): String = Json.parse(getClass.getResourceAsStream(s"/$file")).toString

  val saReturn = loadFile("SAReturnHappy.json")
  val saDebit = loadFile("SADebitHappy.json")
  val commPreferences = loadFile("CommPreferences.json")

  val ddiResponse = loadFile("DDI.json")
  val emptyDDIResponse = loadFile("emptyDDI.json")
  val ddiPPResponse = loadFile("DDIPP.json")

  val validArrangementSubmission = Json.parse(getClass.getResourceAsStream("/validTTPArrangement.json"))
  val validArrangement = Json.fromJson[Arrangement](validArrangementSubmission).get
  val letterAndControl = validArrangement.letterAndControl.get

  val ddiRequestJson = Json.parse(getClass.getResourceAsStream("/DDIRequest.json"))
  val ddiRequest = Json.fromJson[DDIRequest](ddiRequestJson).get

  val validExistingDDI = Json.parse(getClass.getResourceAsStream("/existingDDI.json"))
  val validNewDDIJson = Json.parse(getClass.getResourceAsStream("/newDDI.json"))
  val validNewDDI = Json.fromJson[DDIPPRequest](validNewDDIJson).get

  val yourSubmissionError = "Your submission contains one or more errors"

  val fakeAuthRequest = FakeRequest().withHeaders(AUTHORIZATION -> "Authorised")
  val fakeEnvironmentRequest = FakeRequest().withHeaders("Environment" -> "Environment")
  val fakeAuthEnvironmentRequest = FakeRequest().withHeaders(
    AUTHORIZATION -> "Authorised",
    "Environment" -> "Environment")

}
