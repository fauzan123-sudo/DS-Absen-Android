import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.infinity.dsmabsen.databinding.ItemSpinnerBinding
import com.infinity.dsmabsen.model.DataXXXXXXXXXXXXXXXXXXXXXXXXXXX

class DataSpinnerAdapter(context: Context, data: List<DataXXXXXXXXXXXXXXXXXXXXXXXXXXX>) :
    ArrayAdapter<DataXXXXXXXXXXXXXXXXXXXXXXXXXXX>(context, 0, data) {

    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemSpinnerBinding = ItemSpinnerBinding::inflate

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = convertView?.let { ItemSpinnerBinding.bind(it) } ?: bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        bindData(binding, getItem(position))
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = convertView?.let { ItemSpinnerBinding.bind(it) }
            ?: ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindData(binding, getItem(position))
        return binding.root
    }

    private fun bindData(binding: ItemSpinnerBinding, data: DataXXXXXXXXXXXXXXXXXXXXXXXXXXX?) {
        if (data == null) {
            binding.root.isVisible = false
        } else {
            binding.root.isVisible = true
            binding.root.tag = data
            binding.permissions.text = data.label
//            binding.value.text = data.value.toString()
        }
    }
}
