package d.dao.easylife.dagger2.components;

import d.dao.easylife.dagger2.app.AppComponent;
import d.dao.easylife.dagger2.modules.RobotActivityModule;
import d.dao.easylife.dagger2.presenter.impl.RobotPresenter;
import d.dao.easylife.dagger2.scopes.ActivityScope;
import d.dao.easylife.dagger2.ui.RobotActivity;
import dagger.Component;

/**
 * Created by dao on 6/9/16.
 */
@ActivityScope
@Component(modules =RobotActivityModule.class,dependencies = AppComponent.class)
public interface RobotActivityComponent {

    RobotActivity inject(RobotActivity robotActivity);

    //注入NewsPresenter
    RobotPresenter getRobotPresenter();

}
