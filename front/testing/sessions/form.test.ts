import {FormComponent} from "../../src/app/features/sessions/components/form/form.component";
import {ComponentFixture, TestBed} from "@angular/core/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientModule} from "@angular/common/http";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ReactiveFormsModule} from "@angular/forms";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatSelectModule} from "@angular/material/select";
import {BrowserAnimationsModule, NoopAnimationsModule} from "@angular/platform-browser/animations";
import {SessionService} from "../../src/app/services/session.service";
import {SessionApiService} from "../../src/app/features/sessions/services/session-api.service";
import {expect} from "@jest/globals";
import {Router} from "@angular/router";
import {of, throwError} from 'rxjs';
import {ListComponent} from "../../src/app/features/sessions/components/list/list.component";
import {NgZone} from "@angular/core";

/**
 * Test du composant FormComponent
 */
describe('Component: FormComponent', () => {
    // Déclaration des variables
    let component: FormComponent;
    let fixture: ComponentFixture<FormComponent>;
    let spySubmit: jest.SpyInstance;
    let sessionApiService: SessionApiService;
    let router: Router;
    let spyRouter: jest.SpyInstance;
    let ngZone: NgZone;

    const mockSessionService = {
        sessionInformation: {
            admin: true
        }
    }

    // Avant chaque test on configure le composant et on récupère les services
    beforeEach(async () => {
        await TestBed.configureTestingModule({

            imports: [
                RouterTestingModule.withRoutes([
                    {path: "sessions", component: ListComponent}
                ]),
                HttpClientModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule,
                MatSnackBarModule,
                MatSelectModule,
                BrowserAnimationsModule,
                NoopAnimationsModule
            ],
            providers: [
                {provide: SessionService, useValue: mockSessionService},
                SessionApiService
            ],
            declarations: [FormComponent]
        })
            .compileComponents();

        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        component.sessionForm?.setValue({
            name: 'Name Test',
            description: 'Description Test',
            teacher_id: 1,
            date: new Date(),
        });
        spySubmit = jest.spyOn(component, 'submit');
        sessionApiService = TestBed.inject(SessionApiService);
        router = TestBed.inject(Router);
        spyRouter = jest.spyOn(router, 'navigate');
        ngZone = TestBed.inject(NgZone);
        fixture.detectChanges();
    });

    // Test de la possibilité de soumettre le formulaire
    it('should be able to submit the form', () => {
        component.submit();
        expect(spySubmit).toHaveBeenCalled();
    });

    // Test de la soumission du formulaire avec succès
    it('should create successfully', () => {
        component.onUpdate = false;

        component.sessionForm?.setValue({
            name: 'Name Test',
            description: 'Description Test',
            teacher_id: 1,
            date: new Date(),
        });

        const mockResponse = {
            name: 'Name Test',
            description: 'Description Test',
            teacher_id: 1,
            date: new Date(),
            users: []
        };

        const spyCreate = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockResponse));

        ngZone.run(() => {
            component.submit();
        });

        expect(spyCreate).toHaveBeenCalled();
        expect(spyRouter).toHaveBeenCalledWith(['sessions']);
    });

    // Test de la soumission du formulaire avec une erreur
    it('should not create with an error', () => {
        component.onUpdate = false;

        component.sessionForm?.setValue({
            name: 'Name Test',
            description: 'Description Test',
            teacher_id: 1,
            date: new Date(),
        });

        const spyCreate = jest.spyOn(sessionApiService, 'create').mockImplementation(() => {
            return throwError(() => new Error('Error'));
        });

        ngZone.run(() => {
            component.submit();
        });

        expect(spyCreate).toHaveBeenCalled();
        expect(spyRouter).not.toHaveBeenCalled();
    });

    // Test de la mise à jour du formulaire
    it('should update successfully', () => {
        component.onUpdate = true;
        const date = new Date();

        component.sessionForm?.setValue({
            name: 'Update Test',
            description: 'Update Test',
            teacher_id: 1,
            date: date,
        });

        const mockResponse = {
            name: 'Update Test',
            description: 'Update Test',
            teacher_id: 1,
            date: date,
            users: []
        };

        const spyUpdate = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(mockResponse));

        ngZone.run(() => {
            component.submit();
        });

        expect(spyUpdate).toHaveBeenCalled();
        expect(spyUpdate).toReturn();
        expect(spyRouter).toHaveBeenCalledWith(['sessions']);
        expect({...component.sessionForm?.value, users:[]}).toEqual(mockResponse);
    });

    // Test de la mise à jour du formulaire avec une erreur
    it('should not update with an error', () => {
        component.onUpdate = true;

        component.sessionForm?.setValue({
            name: 'Name Test',
            description: 'Description Test',
            teacher_id: 1,
            date: new Date(),
        });

        const spyUpdate = jest.spyOn(sessionApiService, 'update').mockImplementation(() => {
            return throwError(() => new Error('Error'));
        });

        ngZone.run(() => {
            component.submit();
        });

        expect(spyUpdate).toHaveBeenCalled();
        expect(spyRouter).not.toHaveBeenCalled();
    });

    // Test de la redirection vers la liste des sessions
    it('should redirect to sessions if user is not admin', () => {
        Object.defineProperty(component, 'sessionService', {value: {sessionInformation: {admin: false}}});

        ngZone.run(() => {
            component.ngOnInit();
        });

        expect(spyRouter).toHaveBeenCalledWith(['/sessions']);
    });
});