package com.geekbrains.potd

import com.geekbrains.potd.experiments.DataClassA
import com.geekbrains.potd.experiments.DataClassB
import com.geekbrains.potd.experiments.DataClassBase
import com.google.gson.*
import com.google.gson.internal.bind.JsonTreeReader
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*
import kotlin.reflect.KClass


class DataClassesTest {
    @Test
    fun checkDataClasses() {
        val makeDB = { x: Int, n: Int -> DataClassB(x, DataClassA("Plan", n)) }
        val d1 = makeDB(100, 9)
        val d2 = makeDB(100, 9)
        assertTrue(d1 === d1)
        assertTrue(d1 !== d2)
        assertEquals(d1, d2)
        assertEquals(d1, d1.copy())
        assertTrue(d1 !== d1.copy())
        assertTrue(d1.dataA === d1.copy().dataA)
        assertTrue(d1.copy(x = 99) != d1)
        assertEquals(Gson().toJson(d2), """{"x":100,"dataA":{"a":"Plan","b":9}}""")
        assertTrue(arrayListOf(d1, d2, d1.copy(x = 7)) == arrayListOf(d2, d1, d1.copy(x = 7)))
        assertTrue(arrayListOf(d1, d2, d1.copy(x = 7)) != arrayListOf(d2, d1, d1.copy(x = 19)))
    }

    @Test
    fun checkCopyBase() {
        val a = DataClassA("a", 0)
        val d1: DataClassBase = DataClassB(0, a)
        val d1b: DataClassBase = DataClassB(0, a)
        val d2 = DataClassB(0, a)
        val d3: DataClassBase = a
        val d4: DataClassBase = DataClassB(1, a)
        assertTrue(d1 == d2)
        assertTrue(d1.clone() == d2)
        assertTrue(d1 != d3)
        assertTrue(d1 != d4)
        assertTrue(d1 == d1b)
    }

    @Test
    fun fromJson() {
        val a = DataClassA("Plan", 9)
        assertEquals(Gson().fromJson("""{"a":"Plan","b":9}""", DataClassA::class.java), a)

        // восстановление работает даже с лишним элементом
        assertEquals(Gson().fromJson("""{"a":"Plan","b":9,"c":12}""", DataClassA::class.java), a)
    }

    @Test
    fun roundTrip() {
        val array = arrayListOf(
            DataClassA("Plan", 9),
            DataClassB(9, null),
            DataClassB(8, DataClassA("Plan", 8))
        )
        val round = jsonToData(dataToJson(array))
        assertEquals(array, round)
        array.add(DataClassA("HO", 1))
        assertNotEquals(array, round)
    }

    @Test
    fun roundTrip2() {
        val array = arrayListOf(
            DataClassA("Plan", 9),
            DataClassB(9, null),
            DataClassB(8, DataClassA("Plan", 8))
        )
        val round2 = jsonToData2(dataToJson2(array))
        assertEquals(array, round2)
        array.add(DataClassA("HO", 1))
        assertNotEquals(array, round2)
    }

    // wrapper
    private class DataClassWrapper(x: DataClassBase) {
        private var a: DataClassA? = null
        private var b: DataClassB? = null

        init {
            when (x) {
                is DataClassA -> a = x
                is DataClassB -> b = x
                else -> throw IllegalArgumentException("unsupported dataclass")
            }
        }

        fun toBase(): DataClassBase? = a ?: b
    }

    private fun dataToJson(data: ArrayList<DataClassBase>): String {
        return Gson().toJson(data.map { DataClassWrapper(it) })
    }

    private fun jsonToData(json: String): ArrayList<DataClassBase> {
        val wrappers: ArrayList<DataClassWrapper> =
            Gson().fromJson(json, object : TypeToken<ArrayList<DataClassWrapper>>() {}.type)
        return ArrayList(wrappers.mapNotNull { it.toBase() })
    }

    // reflection
    private fun dataToJson2(data: ArrayList<DataClassBase>): String {
        val result = StringBuilder("[")
        for (i in 0 until data.size) {
            if (i != 0)
                result.append(",")
            val d = data[i]
            result.append(
                "{\"%s\":%s}".format(d::class.simpleName, Gson().toJson(d, d::class.java))
            )
        }
        result.append("]")
        return result.toString()
    }

    private fun jsonToData2(json: String) =
        jsonToData2(json, listOf(DataClassA::class, DataClassB::class))

    private fun jsonToData2(
        json: String,
        types: List<KClass<out DataClassBase>>
    ): ArrayList<DataClassBase> {
        val array: JsonArray = JsonParser().parse(json).asJsonArray
        val outputArray = ArrayList<DataClassBase>()
        for (i in 0 until array.size()) {
            val obj = array.get(i).asJsonObject
            for (t in types) {
                val name = t.simpleName
                if (!obj.has(name))
                    continue
                val result: DataClassBase =
                    Gson().fromJson(JsonTreeReader(obj.getAsJsonObject(name)), t.java)
                outputArray.add(result)
                break
            }
        }
        return outputArray
    }
}