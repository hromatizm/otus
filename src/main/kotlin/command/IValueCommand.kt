package command

interface IValueCommand<T> {

    fun execute(): T
}