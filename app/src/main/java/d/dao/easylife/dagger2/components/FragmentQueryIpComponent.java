package d.dao.easylife.dagger2.components;

import d.dao.easylife.dagger2.app.AppComponent;
import d.dao.easylife.dagger2.modules.QueryIpFragmentModule;
import d.dao.easylife.dagger2.presenter.impl.QueryIpPresenter;
import d.dao.easylife.dagger2.scopes.ActivityScope;
import d.dao.easylife.dagger2.ui.fragment.FragmentQueryIp;
import dagger.Component;

/**
 * Created by dao on 6/9/16.
 */
@ActivityScope
@Component(modules = QueryIpFragmentModule.class,dependencies = AppComponent.class)
public interface FragmentQueryIpComponent {

    FragmentQueryIp inject(FragmentQueryIp fragmentQueryIp);

    //注入NewsPresenter
    QueryIpPresenter getQueryIpPresenter();

}
