package com.example.searchlistcomposeexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.searchlistcomposeexample.viewmodel.model.ListData
import com.example.searchlistcomposeexample.utils.base.BaseComponentActivity
import com.example.searchlistcomposeexample.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val listData = mutableStateOf(mutableListOf(ListData(null,null,null,null)))

    override fun loadingData() {
        super.loadingData()
        mainViewModel.getData()
    }

    override fun observeData() {
        super.observeData()
        mainViewModel.listData.observe(this) {
            parseObserveData(it,
                resultSuccess = {
                    dismissLoadingDialog()
                    it?.let { it1 ->
                        listData.value = it1
                    }
                },
                resultError = {
                    dismissLoadingDialog()
                    Toast.makeText(this, it?.message ?: "", Toast.LENGTH_LONG).show()
                },
                resultNetworkFailed = {
                    dismissLoadingDialog()
                    Toast.makeText(this, it?.message ?: "", Toast.LENGTH_LONG).show()
                }

            )
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setContent {
            Scaffold(
                backgroundColor = colorResource(id = R.color.colorPrimaryDark)
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    Navigation()
                }
            }
        }
    }

    @Composable
    fun Navigation() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen(navController = navController)
            }
            composable(
                "details/{text}",
                arguments = listOf(navArgument("text") { type = NavType.StringType })
            ) { backStackEntry ->
                backStackEntry.arguments?.getString("text")?.let { text ->
                    DetailsScreen(text = text)
                }
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavController) {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        Column {
            SearchView(textState)
            ListData(navController = navController, state = textState)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        val navController = rememberNavController()
        MainScreen(navController = navController)
    }

    @Composable
    fun SearchView(state: MutableState<TextFieldValue>) {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
            },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            placeholder = {
                Text(text = "Search Here..", color = Color.White)
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (state.value != TextFieldValue("")) {
                    IconButton(
                        onClick = {
                            state.value =
                                TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                leadingIconColor = Color.White,
                trailingIconColor = Color.White,
                backgroundColor = colorResource(id = R.color.colorPrimary),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun SearchViewPreview() {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        SearchView(textState)
    }

    @Composable
    fun ListData(navController: NavController, state: MutableState<TextFieldValue>) {
        val datas = listData.value
        var filteredData: MutableList<ListData>
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            val searchedText = state.value.text
            filteredData = if (searchedText.isEmpty()) {
                listData.value
            } else {
                val resultList = mutableListOf<ListData>()
                for (data in datas) {
                    if (data.title?.lowercase(Locale.getDefault())
                            ?.contains(searchedText.lowercase(Locale.getDefault())) == true
                    ) {
                        resultList.add(data)
                    }
                }
                resultList
            }
            items(filteredData) { filteredlist ->
                ListItem(
                    text = filteredlist.title?: "",
                    onItemClick = { selected ->
                        navController.navigate("details/${filteredlist.body}") {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo("main") {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ListPreview() {
        val navController = rememberNavController()
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        ListData(navController = navController, state = textState)
    }

    @Composable
    fun ListItem(text: String, onItemClick: (String) -> Unit) {
        Row(
            modifier = Modifier
                .clickable(onClick = { onItemClick(text) })
                .background(colorResource(id = R.color.colorPrimaryDark))
                .height(57.dp)
                .fillMaxWidth()
                .padding(PaddingValues(8.dp, 16.dp))
        ) {
            Text(text = text, fontSize = 18.sp, color = Color.Black)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ListItemPreview() {
        ListItem(text = "", onItemClick = { })
    }

}