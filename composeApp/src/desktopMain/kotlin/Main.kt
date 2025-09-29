import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.minesweeper.MineSweeperApp
import com.minesweeper.di.commonModule
import org.koin.core.context.GlobalContext.startKoin


fun main() = application {
    startKoin {
        modules(commonModule,)
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "MineSweeperMultiPlatform",
    ) {
        MineSweeperApp()
    }
}
