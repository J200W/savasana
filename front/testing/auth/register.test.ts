import {RegisterComponent} from "../../src/app/features/auth/components/register/register.component";
import {ReactiveFormsModule} from "@angular/forms";
import {expect} from '@jest/globals';
import {ComponentFixture, TestBed, waitForAsync} from "@angular/core/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {AuthService} from "../../src/app/features/auth/services/auth.service";
import {of, throwError} from "rxjs";
import {Router} from "@angular/router";
import {NgZone} from "@angular/core";
import {RouterTestingModule} from "@angular/router/testing";
import {LoginComponent} from "../../src/app/features/auth/components/login/login.component";

/**
 * Test du composant RegisterComponent
 */
describe('Component: RegisterComponent', () => {

    // Déclaration des variables
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let authService: AuthService;
    let spySubmit: jest.SpyInstance;
    let router: Router;
    let spyRouter: jest.SpyInstance;
    let ngZone: NgZone;

    // Avant chaque test on configure le composant et on récupère les services
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RegisterComponent],
            imports: [
                RouterTestingModule.withRoutes([
                    {path: "login", component: LoginComponent}
                ]),
                BrowserAnimationsModule,
                HttpClientModule,
                ReactiveFormsModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule
            ]
        })
            .compileComponents();

        // On récupère les services et on les initialise
        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        authService = TestBed.inject(AuthService);
        router = TestBed.inject(Router);
        ngZone = TestBed.inject(NgZone);
        spySubmit = jest.spyOn(component, 'submit');
        spyRouter = jest.spyOn(router, 'navigate');
        fixture.detectChanges();
    });

    // Test de la possibilité de soumettre le formulaire
    it('should be able to submit the form', () => {
        component.submit();
        expect(spySubmit).toHaveBeenCalled();
    });

    // Test de la soumission du formulaire avec succès
    it('should submit successfully', () => {
        const randomInt = Math.floor(Math.random() * 4000);

        const registerRequest = {
            email: 'test-' + randomInt + '@test.com',
            password: 'test!1234',
            firstName: 'Test',
            lastName: 'Test'
        };

        component.form.setValue(registerRequest);

        const spyRegister =
            jest.spyOn(authService, 'register').mockReturnValue(of(void {}));

        ngZone.run(() => {
            component.submit();
        });

        expect(spySubmit).toHaveBeenCalled();
        expect(spyRegister).toHaveBeenCalled();
        expect(component.onError).toBe(false);
        expect(spyRouter).toHaveBeenCalled();
    });

    // Test de la soumission du formulaire avec une erreur
    it('shouldn\'t submit with an error', () => {
        const registerRequest = {
            email: 'nimportequoi@gmail.com',
            password: '',
            firstName: 'Test',
            lastName: 'Test'
        };

        jest.spyOn(authService, 'register').mockImplementation(() => {
            return throwError(() => new Error('Error'));
        });

        const spy = jest.spyOn(component, 'submit');
        component.form.setValue(registerRequest);
        component.submit();

        expect(spy).toHaveBeenCalled();
        expect(component.onError).toBe(true);
    });
});