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
import play.api.mvc.{Action, AnyContent}

class EligibilityStubController @Inject()() extends ResponseHandling {

  /**
    * Represents the getSAReturns endpoint for DES which is called by Time To Pay Taxpayer
    */
  def generateSAReturns(utr: String): Action[AnyContent] =
    serveFile(pickFileSaReturns(utr))(utr)

  def pickFileSaReturns(utr: String) = utr match{
    case "2131397593" => "/SAReturnsNotSubmitted.json"
    case _ => "/SAReturnHappy.json"
  }
  /**
    * Represents the getSADebits endpoint for DES which is called by Time To Pay Taxpayer
    */
  def generateSADebits(utr: String): Action[AnyContent]= {
    Logger.info("\n\n + utr + \n\n" + utr)
    serveFile(pickFileSaDebits(utr))(utr)
  }
  //8266940627

  def pickFileSaDebits(utr: String) = utr match{
    case "4629044474" => "/SADebitUnHappyInsignificantDept.json"
    case "5419269806" =>  "/SaDebitUnHappyTooMuch.json"
    case "8119337614" =>  "/SaDeptTooOld.json"
    case "2131397593" =>  "/SaDebitNotSubmittedTaxYear.json"
    case _ => "/SADebitHappy.json"
  }

  /**
    * Represents the getCommPreferences endpoint for DES which is called by Time To Pay Taxpayer
    */
  def generateCommPreferences(utr: String): Action[AnyContent] =
    serveFile("/CommPreferences.json")(utr)
}
