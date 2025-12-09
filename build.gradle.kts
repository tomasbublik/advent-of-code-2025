import sun.jvmstat.monitor.MonitoredVmUtil.mainClass

plugins {
    kotlin("jvm") version "2.2.21"
    application
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

application {
    mainClass.set(project.findProperty("mainClass")?.toString() ?: "Day01Kt")
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
