/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

// Needed until bug 1884527 is fixed.
@file:Suppress("DEPRECATION")

package org.mozilla.samples.browser

import android.content.Context
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.engine.gecko.fetch.GeckoViewFetchClient
import mozilla.components.browser.engine.gecko.glean.GeckoAdapter
import mozilla.components.concept.engine.Engine
import mozilla.components.experiment.NimbusExperimentDelegate
import mozilla.components.feature.webcompat.WebCompatFeature
import mozilla.components.feature.webcompat.reporter.WebCompatReporterFeature
import mozilla.components.lib.crash.handler.CrashHandlerService
import mozilla.components.support.base.log.Log
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

/**
 * Helper class for lazily instantiating components needed by the application.
 */
class Components(private val applicationContext: Context) : DefaultComponents(applicationContext) {
    private val runtime by lazy {
        // Allow for exfiltrating Gecko metrics through the Glean SDK.
        val builder = GeckoRuntimeSettings.Builder().aboutConfigEnabled(true)
        builder.telemetryDelegate(GeckoAdapter())
        builder.experimentDelegate(NimbusExperimentDelegate())
        builder.crashHandler(CrashHandlerService::class.java)
        GeckoRuntime.create(applicationContext, builder.build())
    }

    override val engine: Engine by lazy {
        GeckoEngine(applicationContext, engineSettings, runtime).also {
            it.installBuiltInWebExtension("borderify@mozac.org", "resource://android/assets/extensions/borderify/") {
                    throwable ->
                Log.log(Log.Priority.ERROR, "SampleBrowser", throwable, "Failed to install borderify")
            }
            it.installBuiltInWebExtension("testext@mozac.org", "resource://android/assets/extensions/test/") {
                    throwable ->
                Log.log(Log.Priority.ERROR, "SampleBrowser", throwable, "Failed to install testext")
            }
            it.installBuiltInWebExtension("testexttabopen@mozac.org", "resource://android/assets/extensions/test2/") {
                    throwable ->
                Log.log(Log.Priority.ERROR, "SampleBrowser", throwable, "Failed to install testexttabopen")
            }
            WebCompatFeature.install(it)
            WebCompatReporterFeature.install(it)
        }
    }

    override val client by lazy { GeckoViewFetchClient(applicationContext, runtime) }
}
