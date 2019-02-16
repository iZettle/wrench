import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

class Repos {

    companion object {
        @JvmStatic
        fun addRepos(handler: RepositoryHandler) {
            handler.google()
            handler.jcenter()
            handler.maven("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }
}