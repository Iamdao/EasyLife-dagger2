package d.dao.easylife.dagger2.components;

import d.dao.easylife.dagger2.app.AppComponent;
import d.dao.easylife.dagger2.modules.JokeFirstFragmentModule;
import d.dao.easylife.dagger2.modules.MainActivityModule;
import d.dao.easylife.dagger2.presenter.impl.JokeFirstPresenter;
import d.dao.easylife.dagger2.presenter.impl.NewsPresenter;
import d.dao.easylife.dagger2.scope.ActivityScope;
import d.dao.easylife.dagger2.ui.MainActivity;
import d.dao.easylife.dagger2.ui.fragment.PageFragment;
import d.dao.easylife.dagger2.ui.view.IJokeFirstView;
import dagger.Component;

/**
 * Created by dao on 6/9/16.
 */
@ActivityScope
@Component(modules = JokeFirstFragmentModule.class,dependencies = AppComponent.class)
public interface JokeFirstFragmentComponent {

    PageFragment inject(PageFragment pageFragment);

    //注入NewsPresenter
    JokeFirstPresenter getJokeFirstPresenter();

}
