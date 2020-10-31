package app.baianat.com.demoApp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.baianat.com.demoApp.R
import app.baianat.com.vanguard.VanGuard
import app.baianat.com.vanguard.rules.IsNotEmpty
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CasesFactory
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        "onViewCreated".print()



        val a = CasesFactory.watch(nameEditText).forRule(IsNotEmpty("eeeg")).create()

        VanGuard.of(this)?.validate(a)

        checkBtn.setOnClickListener {
            (activity as? FragmentActivity)?.openSecond()
        }

    }





}

