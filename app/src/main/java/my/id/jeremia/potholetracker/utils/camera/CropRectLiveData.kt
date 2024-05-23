package my.id.jeremia.potholetracker.utils.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import org.checkerframework.checker.units.qual.K

class CropRectLiveData<A, B, C, D, E>(
    source1: LiveData<A>,
    source2: LiveData<B>,
    source3: LiveData<C>,
    source4: LiveData<D>,
    private val combine: (data1: A?, data2: B?, data3: C?, data4: D?) -> E
) : MediatorLiveData<E>() {

    private var data1: A? = null
    private var data2: B? = null
    private var data3: C? = null
    private var data4: D? = null


    init {
        super.addSource(source1) {
            data1 = it
            value = combine(data1, data2, data3, data4)
        }
        super.addSource(source2) {
            data2 = it
            value = combine(data1, data2, data3, data4)
        }
        super.addSource(source3) {
            data3 = it
            value = combine(data1, data2, data3, data4)
        }
        super.addSource(source4) {
            data4 = it
            value = combine(data1, data2, data3, data4)
        }
    }

    override fun <S : Any?> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> removeSource(toRemove: LiveData<T>) {
        throw UnsupportedOperationException()
    }
}