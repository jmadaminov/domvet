package com.sablab.domvetdoctor.di

import com.sablab.domvetdoctor.di.addPost.AddPostComponent
import com.sablab.domvetdoctor.di.auth.AuthComponent
import com.sablab.domvetdoctor.di.main.MainComponent
import dagger.Module

/**
 * Created by jahon on 09-Apr-20
 */

@Module(subcomponents = [AuthComponent::class, MainComponent::class, /*AddCarComponent::class,*/ AddPostComponent::class])
class SubComponentsModule