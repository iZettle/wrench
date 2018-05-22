import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

class Repos {

    companion object {
        @JvmStatic
        fun addRepos(handler: RepositoryHandler) {
            handler.google()
            handler.jcenter()
            handler.maven { t: MavenArtifactRepository ->
                t.url = URI.create("https://oss.sonatype.org/content/repositories/snapshots")
            }
        }
    }
}