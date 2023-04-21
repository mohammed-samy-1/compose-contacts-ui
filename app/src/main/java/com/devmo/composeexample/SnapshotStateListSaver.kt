package com.devmo.composeexample

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.snapshots.SnapshotStateList

class SnapshotStateListSaver<T : Any> : Saver<SnapshotStateList<T>, List<T>> {
    override fun restore(value: List<T>): SnapshotStateList<T> {
        return SnapshotStateList()
    }

    override fun SaverScope.save(value: SnapshotStateList<T>): List<T> {
        return value.toList()
    }

}