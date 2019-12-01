package com.ace.homework2.di

import android.content.Context
import com.ace.homework2.TFSApplication
import com.ace.homework2.di.action.ActionModule
import com.ace.homework2.di.boards.BoardsModule
import com.ace.homework2.di.cards.CardModule
import com.ace.homework2.di.details.DetailsModule
import com.ace.homework2.di.members.CardsMembersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        FragmentBuildersModule::class,
        BoardsModule::class,
        ActionModule::class,
        CardModule::class,
        DetailsModule::class,
        CardsMembersModule::class,
        ViewModelFactoryModule::class]
)
interface AppComponent : AndroidInjector<TFSApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }
}
