import com.example.gymapp.data.UserProgram

fun progressWeek(program: UserProgram): UserProgram {
    return program.copy(
        currentWeek = program.currentWeek + 1,
       /* squatCurrent = program.squatCurrent + 2,
        benchCurrent = program.benchCurrent + 2,
        deadliftCurrent = program.deadliftCurrent + 5*/
    )
}
