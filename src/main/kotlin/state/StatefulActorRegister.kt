package state

import command.ICommand
import ioc.Ioc
import state.cmd.GetStatefulActorCmd
import state.cmd.RegisterStatefulActorCmd
import spring.ActorId
import state.cmd.StatefulActorStartCmd
import java.util.concurrent.BlockingDeque

class StatefulActorRegister {
    companion object {

        private val actorMap: MutableMap<ActorId, IStatefulActor> = mutableMapOf()

        private val commands = listOf(
            Ioc.resolve<ICommand>(
                dependencyName = "Scopes.Current",
                args = arrayOf("game_1")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Добавить актор", { params: Array<out Any> ->
                    RegisterStatefulActorCmd(
                        actor = params[0] as IStatefulActor,
                        actorMap = actorMap
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Актор", { params: Array<out Any> ->
                    GetStatefulActorCmd(
                        actorId = params[0] as String,
                        actorMap = actorMap
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Запустить актор", { params: Array<out Any> ->
                    StatefulActorStartCmd(
                        actorId = params[0] as String,
                        deque = params[1] as BlockingDeque<IStatefulActorCommand>,
                        actionAfterStart = params[2] as () -> Unit
                    )
                })
            ),
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}