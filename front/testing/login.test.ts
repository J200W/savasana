import {LoginComponent} from "../src/app/features/auth/components/login/login.component";
import {SessionService} from "../src/app/services/session.service";
import {expect} from '@jest/globals';
import {ComponentFixture, TestBed, waitForAsync} from "@angular/core/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatFormFieldModule} from "@angular/material/form-field";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {ReactiveFormsModule} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../src/app/features/auth/services/auth.service";


describe('Component: LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authService: AuthService;
    let router: Router;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [SessionService],
            imports: [
                RouterTestingModule,
                BrowserAnimationsModule,
                HttpClientModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule
            ]
        })
            .compileComponents();
        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        authService = TestBed.inject(AuthService);
        router = TestBed.inject(Router);
        fixture.detectChanges();
    });

    afterEach(() => {
        localStorage.removeItem('token');
    });

    it('should be able to submit the form', () => {
        const spy = jest.spyOn(component, 'submit');
        component.submit();
        expect(spy).toHaveBeenCalled();
    });

    it('should submit successfully', () => {
        const loginRequest = {
            email: 'yoga@studio.com',
            password: 'test!1234'
        };

        const spy = jest.spyOn(component, 'submit');
        const spyNavigate = jest.spyOn(router, 'navigate');

        component.form.setValue(loginRequest);
        component.submit();
        expect(spy).toHaveBeenCalled();
        expect(component.onError).toBe(false);
    });

    it('should submit with error', () => {
        const loginRequest = {
            email: 'nimportequoi@gmail.com',
            password: 'test!1234'
        };

        const spy = jest.spyOn(component, 'submit');
        component.form.setValue(loginRequest);
        component.submit();

        expect(spy).toHaveBeenCalled();
        expect(component.onError).toBe(false);
    });
})
;
