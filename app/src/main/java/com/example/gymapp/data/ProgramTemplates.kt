package com.example.gymapp.data

object ProgramTemplates {

    fun fiveByFive(): List<Program> {
        val description = """
5x5 Beginner program je klasičan linearni strength program namenjen početnicima.

Trening se bazira na 5 serija po 5 ponavljanja za glavne vežbe (čučanj, bench press, deadlift).

Početna težina se postavlja na oko 50–70% od vašeg 1RM (one rep max), 
kako bi se omogućila pravilna tehnika i progresivno povećanje opterećenja.

Progresija:
- Svake nedelje dodaje se 2.5 kg na bench i overhead press.
- Svake nedelje dodaje se 5 kg na squat i deadlift.
- Kada se ne uspe završiti svih 5x5, preporučuje se deload od 10%.

Cilj programa je izgradnja osnovne snage, tehnike i mišićne mase kroz linearnu progresiju.
        """.trimIndent()
        val exercises = listOf(
            ExerciseSet("Squat", 5, 5, 75),
            ExerciseSet("Bench", 5, 5, 75),
            ExerciseSet("Deadlift", 3, 5, 75)
        )
        return listOf(
            Program("five_by_five", "5x5 Beginner", exercises, description)
        )
    }

    fun fiveThreeOne(): List<Program> {
        val description = """
5/3/1 je napredni strength program zasnovan na periodizaciji i radu sa procentima.

Program koristi 90% vašeg 1RM kao Training Max (TM), 
a radne težine se računaju iz tog broja.

Ciklus traje 4 nedelje:

Nedelja 1: 3x5 (65%, 75%, 85%)
Nedelja 2: 3x3 (70%, 80%, 90%)
Nedelja 3: 5/3/1 (75%, 85%, 95%)
Nedelja 4: Deload (40–60%)

Poslednja serija svake nedelje je AMRAP (što više ponavljanja uz dobru formu).

Progresija:
- +2.5 kg na upper body vežbe po ciklusu
- +5 kg na lower body vežbe po ciklusu

Cilj programa je dugoročni napredak uz kontrolisan umor i pametnu progresiju.
        """.trimIndent()
        val exercises = listOf(
            ExerciseSet("Squat", 3, 5, 65),
            ExerciseSet("Bench", 3, 5, 65),
            ExerciseSet("Deadlift", 3, 5, 65)
        )
        return listOf(
            Program("five_three_one", "5/3/1", exercises, description)
        )
    }

    fun hybrid(): List<Program> {
        val description = """
Hybrid Strength je kombinacija maksimalne snage i volumena.

Svaki trening se sastoji iz:

1. Jednog teškog top seta (1x1 ili 1x3) na 85–92% 1RM
2. Tri radne serije po 3 ponavljanja na oko 80%
3. Dva back-off seta od 5–6 ponavljanja na 65–75%

Ovakav pristup omogućava:
- Razvoj maksimalne snage (heavy set)
- Tehničku stabilnost pod velikim opterećenjem
- Dodatni volumen za hipertrofiju

Progresija:
- Povećavati top set za 2.5–5 kg kada se uspešno završi
- Back-off težine prilagođavati proporcionalno

Program je idealan za srednje i napredne vežbače koji žele balans između snage i mišićne mase.
        """.trimIndent()
        val exercises = listOf(
            ExerciseSet("Squat", 1, 1, 90), // Težak set
            ExerciseSet("Squat", 3, 3, 80),
            ExerciseSet("Squat", 2, 6, 70), // Back-off
            ExerciseSet("Bench", 1, 1, 90),
            ExerciseSet("Bench", 3, 3, 80),
            ExerciseSet("Bench", 2, 6, 70),
            ExerciseSet("Deadlift", 1, 1, 90),
            ExerciseSet("Deadlift", 3, 3, 80),
            ExerciseSet("Deadlift", 2, 6, 70)
        )
        return listOf(
            Program("Hybrid_Strength", "Hybrid Strength", exercises, description)
        )
    }
}
